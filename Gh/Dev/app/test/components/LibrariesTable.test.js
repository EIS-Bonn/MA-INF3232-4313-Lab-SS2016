import React from 'react';
import { expect } from 'chai';
import { shallow, mount, render } from 'enzyme';

import LibrariesTable from '../../src/js/components/table/LibrariesTable';

describe("(Component) LibrariesTable", function() {

  const props = {
    tcontent: [],
    theaders: [],
    tabsList: [],
    activeFilter: 'testFilter'
  }

  const wrapper = shallow(<LibrariesTable {...props} />);

  it('renders without exploding', () => {
    expect(wrapper).to.have.length(1);
  });

  it('renders with a table', () => {
    expect(wrapper.find('.table')).to.have.length(1);
  });

});
