import React from 'react';
import { expect } from 'chai';
import sinon from 'sinon';
import { shallow, mount, render } from 'enzyme';

import Dropdown from '../../src/js/components/sidebar/Dropdown';

describe("(Component) Dropdown", function() {

  const props = {
    value: 'new title',
    dropdownList: [{id: 1, name: 'first', value: 'first'}, {id: 2, name: 'last', value: 'last'}]
  }

  it('renders without exploding', () => {
    const wrapper = shallow(<Dropdown {...props} />);

    expect(wrapper).to.have.length(1);
  });

  it('has a title', () => {
    const wrapper = mount(<Dropdown {...props} />);

    expect(wrapper.props().value).to.equal('new title');
  });

  it('has a dropdown list', () => {
    const wrapper = mount(<Dropdown {...props} />);

    expect(wrapper.find('.dropdown-menu > li')).to.have.length.above(1);

  });

  it('does open the dropdown if clicked', () => {
    const wrapper = mount(<Dropdown {...props} />);

    expect(wrapper.state().isOpened).to.equal(false);

    wrapper.find('button').simulate('click');

    expect(wrapper.state().isOpened).to.equal(true);
  });


  it('can change current title', () => {
    const wrapper = mount(<Dropdown {...props} />);

    expect(wrapper.props().value).to.equal('new title');

    const newTitle = wrapper.find('li').first().text();
    wrapper.setProps({value: newTitle});

    expect(wrapper.props().value).to.equal(newTitle);
  });


});
