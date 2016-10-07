import React from 'react';
import { expect } from 'chai';
import { shallow, mount, render } from 'enzyme';

import FeaturesSet from '../../src/js/components/sidebar/FeaturesSet';

describe("(Component) FeaturesSet", function() {

  const props = {
    title: 'new title',
    colour: 'black',
    filters: [],
    isActive: false
  };

  it('renders without exploding', () => {
    const wrapper = shallow(<FeaturesSet {...props }/>);

    expect(wrapper).to.have.length(1);
  });

  it('has a title', () => {
    const wrapper = mount(<FeaturesSet {...props} />);

    expect(wrapper.props().title).to.equal('new title');
  });

  it('has a colour', () => {
    const wrapper = mount(<FeaturesSet {...props} />);

    expect(wrapper.props().colour).to.equal('black');
  });

  it('could change isActive state', () => {
    const wrapper = mount(<FeaturesSet {...props} />);

    expect(wrapper.props().isActive).to.equal(false);

    wrapper.setProps({isActive: true});

    expect(wrapper.props().isActive).to.equal(true);
  });

});
