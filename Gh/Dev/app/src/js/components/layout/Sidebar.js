import React, { Component, PropTypes } from 'react';

import FeaturesSet from '../sidebar/FeaturesSet';

class Sidebar extends Component {
  constructor(props) {
    super(props);
  }

  applyFilters(e){
    e.preventDefault();
    this.props.applyFilters();
  }

  shouldComponentUpdate(nextProps, nextState) {
    return nextProps.activeFiltersSet != this.props.activeFiltersSet || nextProps.querySize != this.props.querySize;
  }

  render() {
    const featuresSets = this.props.features.map(item => {
      const isActive = (item.ref === this.props.activeFiltersSet) ? true : false;

      return <FeaturesSet
              key={item.ref}
              title={item.title}
              colour={item.colour}
              filters={item.filters}
              isActive={isActive}
              onReset={this.props.onReset}
              onChange={this.props.onChange}
               />;
    });

    return (
      <div id="sidebar-wrapper">
        <h2>FILTERS</h2>
        <div class='features-set filter-buttons'>
          <button type="button"
            class={`btn btn-block btn-green ${this.props.querySize > 0 ? 'active' : 'disabled'}`}
            onClick={this.applyFilters.bind(this)}>Apply</button>
        </div>

        {featuresSets}

      </div>
    );
  }

}

Sidebar.propTypes = {
  features: PropTypes.array.isRequired,
  activeFiltersSet: PropTypes.string.isRequired,
  querySize: PropTypes.number.isRequired,
  applyFilters: PropTypes.func
};

export default Sidebar;
