/* global $ */

import 'babel-polyfill';
import _ from 'lodash';
// import $ from 'jquery';
import moment from 'moment';

import 'semantic-ui/dist/semantic.js';
import 'semantic-ui/dist/semantic.css';

import * as config from './config';
import map from './map';
import * as db from './store';
import './index.css';


// open file from uploader input and load data into rdfstore
function openFile() {
  const reader = new FileReader();

  let content = '';
  const filePath = this;

  if (filePath.files && filePath.files[0]) {
    reader.onload = (ev) => {
      content = ev.target.result;
      db.setData(content);
      // Get the processes and display them
      db.queryAll().then(t => {
        displayAllProcesses(t);
        console.log(t);
      });
    };

    reader.readAsText(filePath.files[0]);
  }
  return true;
}

function displayAllProcesses(res) {
  if (res.length > 0) {
    const processes = {};

    for (let i = 0; i < res.length; i++) {
      // More than one node belongs to one process
      if (res[i].p.value in processes) {
        processes[res[i].p.value].points.push([res[i].lat.value, res[i].long.value]);
        processes[res[i].p.value].names.push(res[i].name.value);
        processes[res[i].p.value].types.push(res[i].type.value);
      // Havent seen the process before
      } else {
        const st = moment(res[i].st.value);
        const et = moment(res[i].et.value);
        const duration = et.from(st);

        processes[res[i].p.value] = {
          pType: res[i].pType.value.replace(config.prefixes.scor, ''),
          supplier: res[i].supplier.value,
          productName: res[i].pn.value,
          st,
          et,
          duration,
          points: [[res[i].lat.value, res[i].long.value]],
          names: [res[i].name.value],
          types: [res[i].type.value],
        };
      }
    }

    _.forOwn(processes, (v, k) => {
      map.addProcess(k, v);
    });
  }
}

function formatDate(dateStr) {
  if (!dateStr) return undefined;
  try {
    return moment(dateStr).format('YYYY-MM-DDTHH:mm:ss');
  } catch (err) {
    console.log(err);
    return undefined;
  }
}

function submitAnalyse(e) {
  if (e.preventDefault) e.preventDefault();

  // Clear previous results
  map.clearLabels();

	// they were like this in the past
	// const processType = $('#analyzeForm input[name=processTypeInput]')[0].value;
	// they were changed to this, since the selector could not find the input.
  // tested on Chrome
  const supplier = $('input[name=supplierInput]').val();
  const productName = $('input[name=productNameInput]').val();
  const startTime = formatDate($('input[name=startTimeInput]').val());
  const endTime = formatDate($('input[name=endTimeInput]').val());
  const metricName = $('input[name=metricInput]').val();
  const processType = $('input[name=processTypeInput]').val();

  // Build a query according to the filters and add corresponding results as labels
  db.buildQuery(processType, metricName, { supplier, productName, startTime, endTime }).then(res => {
    console.log(res);
    res.forEach((v) => {
      map.addLabelToProcess(v.p.value, `${v.metricResult.value}`);
    });
  });
}

/** Clear labels on process or metric change. */
function handleProcessChange() {
  map.clearLabels();
  const processType = $('input[name=processTypeInput]').val();
  console.log(processType);

  $('#metricSelection').replaceWith(config.processMetrics[processType]);
  $('.ui.dropdown').dropdown();
}

function handleMetricChange() {
  map.clearLabels();
}

$('#upload-button').click(() => $('#dataFile').click());

// $('#queryForm').submit(submitQuery);

$('#dataFile').change(openFile);

$('#analyzeForm').submit(submitAnalyse);

$('#processSelection').change(handleProcessChange);

$('#metricSelection').change(handleMetricChange);

$(document).ready(() => {
  $('.ui.dropdown').dropdown();
});

module.exports = {
  openFile,
};
