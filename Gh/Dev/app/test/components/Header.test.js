import React from 'react';
import { expect } from 'chai';
import { shallow, mount, render } from 'enzyme';

import Header from '../../src/js/components/layout/Header';

describe("(Component) Header", function() {

  const props = {
      location: {pathname: '/about'}
  };

  const wrapper = shallow(<Header {...props} />);

  it('renders without exploding', () => {
    expect(wrapper).to.have.length(1);
  });

  it('has only 1 opened nav item', () => {
    expect(wrapper.find('.open').length).to.be.at.most(1);
  });

});
