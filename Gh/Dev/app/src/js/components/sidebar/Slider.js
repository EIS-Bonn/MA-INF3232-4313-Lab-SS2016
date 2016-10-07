import React, { Component } from 'react';

class Slider extends Component {

  render(){

    const label = <label>{this.props.title} &gt; {this.props.value} {this.props.units}</label>;

    return (
      <div class="filter">
        <p>{label}
          <input type='range'
                 min={this.props.min}
                 max={this.props.max}
                 step={this.props.step}
                 value={this.props.value}
                 onChange={e => this.props.onChange(e.target.value)} />
        </p>
      </div>
    );
  }
}

export default Slider;
