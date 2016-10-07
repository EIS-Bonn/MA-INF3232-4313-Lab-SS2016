import React, { Component } from 'react';
import { connect } from 'react-redux';

import * as actions from '../actions/index';

import LibrariesTable from '../components/table/LibrariesTable';
import PageTitle from '../components/layout/PageTitle';
import Sidebar from '../components/layout/Sidebar';
import SearchResult from '../components/table/SearchResult';

const queryMap = new Map();

export class Libraries extends Component {
  constructor(props) {
    super(props);
    this.state = {
      pageTitle: 'Comparison of RDF JavaScript libraries',
      searchResult: 0,
      activeFiltersSet: 'general',
      querySize: 0,
      isVisibleSearch: true
    };
  }

  componentWillMount() {
    this.props.getTableTabs();
    this.props.getFeatureSets();
    this.updateTableData(this.state.activeFiltersSet);
  }

  componentDidUpdate(){
    const searchResult = this.props.tabledata.content.length;
    if(this.state.searchResult != searchResult) this.setState({searchResult});
  }

  updateTableData(filterSet){
    this.props.getTableHeaderList(filterSet);
    this.props.getTableContent(filterSet);
  }

  setActiveFilter(activeFiltersSet){
    this.setState({activeFiltersSet});
    this.updateTableData(activeFiltersSet);
    this.clearQueryMap();
  }

  resetFilters(){
    this.clearQueryMap();
    this.applyFilters();
  }

  updateQueryMap(key, value){
    queryMap.set(key, value);
    this.setState({querySize: queryMap.size});
  }

  clearQueryMap(){
    queryMap.clear();
    this.setState({querySize: queryMap.size});
  }

  applyFilters(){
    this.props.queryTable(this.state.activeFiltersSet, queryMap);
    this.setState({isVisibleSearch: true});
  }

  hideSearch(){
    this.setState({isVisibleSearch: false});
  }

  render() {
    const { fetchstatus } = this.props;
    const isLoaded = Object.keys(fetchstatus).every(key => fetchstatus[key]);

    if(!isLoaded) return (<div class='loader'></div>);

    return (
      <div id="wrapper">

        <Sidebar
          querySize={this.state.querySize}
          activeFiltersSet={this.state.activeFiltersSet}
          features={this.props.features}
          onReset={this.resetFilters.bind(this)}
          onChange={this.updateQueryMap.bind(this)}
          applyFilters={this.applyFilters.bind(this)}
          />

        <div id="page-content-wrapper">
          <div class="container-fluid">

            <PageTitle pageTitle={this.state.pageTitle} />

            <SearchResult
              results={this.state.searchResult}
              isVisible={this.state.isVisibleSearch}
              hideSearch={this.hideSearch.bind(this)}
              />

            <div class="row">
              <LibrariesTable
                activeFilter={this.state.activeFiltersSet}
                tabsList={this.props.tabledata.tabs}
                theaders={this.props.tabledata.headers}
                tcontent={this.props.tabledata.content}
                setActiveFilter={this.setActiveFilter.bind(this)}
                />
            </div>

          </div>
        </div>
      </div>
    );
  }
}

export default connect(state => state, actions)(Libraries);
