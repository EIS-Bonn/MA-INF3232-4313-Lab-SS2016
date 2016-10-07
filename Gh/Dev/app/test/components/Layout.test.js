import React from 'react';
import { expect } from 'chai';
import { shallow, mount, render } from 'enzyme';

import Layout from '../../src/js/pages/Layout';

describe("(Component) Layout", function() {

  const props = {
      location: {pathname: '/about'}
  };

  it('renders without exploding', () => {
    const wrapper = shallow(<Layout {...props} />);

    expect(wrapper).to.have.length(1);
  });

  it('recognizes the location', () => {
    const wrapper = mount(<Layout {...props}/>);

    expect(wrapper.props().location.pathname).to.equal('/about');
  });

});
