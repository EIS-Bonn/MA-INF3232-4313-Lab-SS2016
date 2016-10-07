import React from 'react';
import { expect } from 'chai';
import { shallow, mount, render } from 'enzyme';

import Documentation from '../../src/js/pages/Documentation';

describe("(Component) Documentation", function() {

  const wrapper = shallow(<Documentation/>);

  it('renders without exploding', () => {
    expect(wrapper).to.have.length(1);
  });

  it('has a page title', () => {
    expect(wrapper.find('PageTitle')).to.have.length(1);
  });

  it('contains a user manual ', () => {
    expect(wrapper.find('.user-manual')).to.have.length(1);
  });

  it('contains developer notes', () => {
    expect(wrapper.find('.developer-notes')).to.have.length(1);
  });

});
