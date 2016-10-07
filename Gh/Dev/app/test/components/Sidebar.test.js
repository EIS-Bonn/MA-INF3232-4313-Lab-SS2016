import React from 'react';
import { expect } from 'chai';
import sinon from 'sinon';
import { shallow, mount, render } from 'enzyme';

import Sidebar from '../../src/js/components/layout/Sidebar';

describe("(Component) Sidebar", function() {

  const props = {
      activeFiltersSet: 'test',
      querySize: 1,
      features: []
  };

  it('renders without exploding', () => {
    const wrapper = shallow(<Sidebar {...props} />);

    expect(wrapper).to.have.length(1);
  });

  it('Apply Filter button simulates click events', () => {
    const spy = sinon.spy();
    const wrapper = mount(<Sidebar {...props} applyFilters={spy} />);

    wrapper.find('button').simulate('click');
    expect(spy.calledOnce).to.be.true;
  });

  it('Apply Filter button is disabled if querySize < 1', () => {
    const wrapper = mount(<Sidebar {...props} querySize={0} />);

    expect(wrapper.find('button').hasClass('disabled')).to.be.true;
    expect(wrapper.find('button').hasClass('active')).to.be.false;

    wrapper.setProps({querySize: 5});

    expect(wrapper.find('button').hasClass('disabled')).to.be.false;
    expect(wrapper.find('button').hasClass('active')).to.be.true;
  });

});
