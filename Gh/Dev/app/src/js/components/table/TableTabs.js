import React, { PropTypes } from 'react';

const TableTabs = ({tabsList, activeTab, setActiveTab}) => {

    const tabslist = tabsList.map(tab => {
      return <li key={tab.id}
                 class={`${activeTab === tab.title ? 'active' : ''} ${tab.colour}`}
                 onClick={() => setActiveTab(tab.title)}>
                 <a href="#">{tab.name}</a>
             </li>;
    });

    return (
      <ul class="nav nav-tabs">
        {tabslist}
      </ul>
    );

};

TableTabs.propTypes = {
  tabsList: PropTypes.array.isRequired,
  activeTab: PropTypes.string.isRequired,
  setActiveTab: PropTypes.func
};

export default TableTabs;
