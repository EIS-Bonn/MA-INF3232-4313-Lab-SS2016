import React from 'react';
import { expect } from 'chai';
import { shallow, mount, render } from 'enzyme';

import About from '../../src/js/pages/About';

describe("(Component) About", function() {

  const wrapper = shallow(<About/>);

  it('renders without exploding', () => {
    expect(wrapper).to.have.length(1);
  });

  it('has a page title', () => {
    expect(wrapper.find('PageTitle')).to.have.length(1);
  });

});
