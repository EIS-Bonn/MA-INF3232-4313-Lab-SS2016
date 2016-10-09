import leaflet from 'leaflet';
import 'leaflet/dist/leaflet.css';
import _ from 'lodash';

import factoryIcon from './img/factory.png';
import farmIcon from './img/rye.png';
import siloIcon from './img/silo.png';
import windMillIcon from './img/wind-mill.png';
import mineIcon from './img/pick-and-shovel.png';
import powerPlantIcon from './img/power-plant.png';

import './index.css';

class Map {
  constructor(container = 'mapid', initialView = [50.73211, 7.09305], initialZoom = 7) {
    // Manually set the image path for leaflet icons
    leaflet.Icon.Default.imagePath = '/leaflet/dist/images';

    this.el = leaflet.map(container).setView(initialView, initialZoom);

    const accessToken = 'pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpandmbXliNDBjZWd2M2x6bDk3c2ZtOTkifQ._QA7i5Mpkd_m30IGElHziw';

    leaflet.tileLayer(`https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=${accessToken}`, {
      maxZoom: 18,
      attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
        '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
        'Imagery © <a href="http://mapbox.com">Mapbox</a>',
      id: 'mapbox.streets',
    }).addTo(this.el);

    this.popup = leaflet.popup();
    this.el.on('click', this.onMapClick.bind(this));

    this.procTypes = {
      DeliverStockedProduct: {
        color: '#2980b9',
      },
      SourceMakeToOrderProduct: {
        color: '#f1c40f',
      },
      MakeToOrder: {
        color: '#c0392b',
      },
    };
    this.colors = [
      '#7f8c8d', '#2ecc71', '#3498db', '#9b59b6', '#34495e',
      '#e74c3c', '#ecf0f1', '#2c3e50', '#f1c40f', '#1abc9c',
    ];
    this.processes = {};
    this.labeledProcs = [];

    // Info legend
    const legend = leaflet.control({ position: 'bottomright' });
    legend.onAdd = () => {
      const div = leaflet.DomUtil.create('div', 'info legend');

      // loop through our density intervals and generate a label with a colored square for each interval
      _.forEach(this.procTypes, (v, k) => {
        div.innerHTML += `<i style="background: ${v.color}"></i> ${k}<br>`;
      });

      return div;
    };
    legend.addTo(this.el);

    // Specifying new icon to present the anchors
    const customIcon = leaflet.Icon.extend({
      options: {
        iconSize: [32, 32], // size of the icon
        iconAnchor: [16, 16], // point of the icon which will correspond to marker's location
        popupAnchor: [-3, -15], // point from which the popup should open relative to the iconAnchor
      },
    });
    this.icons = {
      factory: new customIcon({ iconUrl: factoryIcon }),
      farm: new customIcon({ iconUrl: farmIcon }),
      silo: new customIcon({ iconUrl: siloIcon }),
      windMill: new customIcon({ iconUrl: windMillIcon }),
      mine: new customIcon({ iconUrl: mineIcon }),
      powerPlant: new customIcon({ iconUrl: powerPlantIcon }),
    };
  }

  onMapClick(e) {
    this.popup
      .setLatLng(e.latlng)
      .setContent(`You clicked the map at geo: ${e.latlng.toString()}`)
      .openOn(this.el);
  }

  /*
   * Add a process initially to the map, keep records of it
   * for further manipulation
   */
  addProcess(uid, obj) {
    const p = obj;
    const latlngs = [];

    // Choose process line color based on process type
    const color = this.procTypes[p.pType].color;
    p.color = color;
    p.markers = [];
    p.info = `
      <ul>
        <li>Process Type: ${p.pType}</li>
        <li>Supplier: ${p.supplier}</li>
        <li>Product name: ${p.productName}</li>
        <li>Start Time:  ${p.et.format('ddd, MMM Do YYYY, HH:mm:ss')}</li>
        <li>End Time:  ${p.st.format('ddd, MMM Do YYYY, HH:mm:ss')}</li>
        <li>Duration: ${p.duration}</li>
      </ul>
      `;

    p.points.forEach((val, i) => {
      let icon = this.icons[p.types[i]];
      if (!icon) icon = this.icons.factory; // Default icon is factory
      const marker = leaflet.marker(val, { icon }).addTo(this.el);
      marker.bindPopup(`
        <ul>
          <li>Name: ${p.names[i]}</li>
          <li>Type: ${p.types[i]}</li>
        </ul>
      `);

      latlngs.push(marker.getLatLng());
      p.markers.push(marker);
    });
    p.line = leaflet.polyline(latlngs, { color, weight: 10, opacity: 0.7 }).addTo(this.el)
      .bindPopup(p.info);

    this.processes[uid] = p;
  }

  /*
   * Add the results of analysis as a label to
   * the lines of a process.
   */
  addLabelToProcess(uid, label) {
    this.processes[uid].line.bindTooltip(label, { permanent: true });
    this.labeledProcs.push(uid);
  }

  /*
   * Upon new analysis, clears the previous labels
   */
  clearLabels() {
    _.forEach(this.labeledProcs, (val) => {
      this.processes[val].line.unbindTooltip();
    });
  }
}


const map = new Map();

export default map;
