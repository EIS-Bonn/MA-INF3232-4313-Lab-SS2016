import React, { Component, PropTypes } from 'react';
import { Table } from 'reactabular';

import TableTabs from './TableTabs';

class LibrariesTable extends Component {
  constructor(props) {
    super(props);
    this.state = {
      activeTab: this.props.activeFilter,
    };
  }

  setActiveTab(newTab){
    if(newTab != this.state.activeTab){
      this.setState({activeTab: newTab});
      this.props.setActiveFilter(newTab);
    }
  }

  formatSize(bytes) {
   const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
   if (bytes == 0) return 'n/a';
   const i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
   return Math.round(bytes / Math.pow(1024, i), 2) + ' ' + sizes[i];
 }

  render() {
    const extraColumns = [
      {
        property: 'sourceSize',
        header: { label: 'Size', props: { style: { width: 100 }} },
        cell: { format: (sourceSize) => this.formatSize(sourceSize)}
      }, {
        property: 'link',
        header: { label: 'Link', props: { style: { width: 70 }} },
        cell: { format: (link) => <a href={link} rel='noopener noreferrer' target='_blank'>Link <i class='fa fa-link'></i></a>}
    }];

    const rows = this.props.tcontent;
    const columns = [...this.props.theaders, ...extraColumns];

    return (
      <div class="col-lg-12">

        <TableTabs
          tabsList={this.props.tabsList}
          activeTab={this.state.activeTab}
          setActiveTab={this.setActiveTab.bind(this)}
          />

        <div class="table-responsive">
          <Table.Provider class="pure-table pure-table-striped table table-bordered table-hover"
            columns={columns}>
            <Table.Header />

            <Table.Body
                rows={rows}
                rowKey="id" />
          </Table.Provider>
        </div>
    </div>
    );
  }

}

LibrariesTable.propTypes = {
  tabsList: PropTypes.array.isRequired,
  tcontent: PropTypes.array.isRequired,
  theaders: PropTypes.array.isRequired,
  activeFilter: PropTypes.string.isRequired
};

export default LibrariesTable;
