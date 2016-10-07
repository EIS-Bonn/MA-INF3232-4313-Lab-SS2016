import React, { Component, PropTypes } from 'react';
import ReactDOM from 'react-dom';

import Checkbox from './Checkbox';
import Dropdown from './Dropdown';
import Slider from './Slider';

const filtersHash = {
  checkbox: Checkbox,
  dropdown: Dropdown,
  slider: Slider
};

class FeaturesSet extends Component {
  constructor(props) {
    super(props);
    this.state = props.filters.reduce((obj, item) => {
        if(item.type === 'dropdown'){ obj[item.term] = item.title;}
        else if(item.type === 'checkbox'){ obj[item.term] = item.isChecked;}
        return obj;
    }, {});
    this.initState = this.state;
  }

  onReset(){
    this.setState(this.initState);
    this.props.onReset();
  }

  onChange(item, value){
    this.setState({ [item.term]: value});
    this.props.onChange(item.term, value);
  }

  render() {
    let { filters } = this.props;

      filters = filters.map(item => {
        const Filter = filtersHash[item.type];
        return <Filter
          key={item.id}
          {...item}
          value={this.state[item.term]}
          onChange={this.onChange.bind(this, item)}
          />;
      });

    return (
      <div class={`${(this.props.isActive === true) ? 'active' : 'box'} ${this.props.colour}`}>
        <div>
          <h3>
            {this.props.title}
            <button class="btn-reset pull-right" onClick={this.onReset.bind(this)}>Reset</button>
          </h3>
          <hr />
        </div>

        {filters}

      </div>
    );
  }

}

FeaturesSet.propTypes = {
  title: PropTypes.string.isRequired,
  colour: PropTypes.string.isRequired,
  filters: PropTypes.array.isRequired,
  isActive: PropTypes.bool.isRequired,
  onReset: PropTypes.func,
  onChange: PropTypes.func
};

export default FeaturesSet;
