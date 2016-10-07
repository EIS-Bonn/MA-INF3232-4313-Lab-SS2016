import React from 'react';
import { expect } from 'chai';
import { shallow, mount, render } from 'enzyme';

import TableTabs from '../../src/js/components/table/TableTabs';

describe("(Component) TableTabs", function() {

  const props = {
    tabsList: [{title: 'active', colour: 'transparent', name: 'test', id: 1}],
    activeTab: 'active'
  }

  const wrapper = shallow(<TableTabs {...props} />);

  it('renders without exploding', () => {
    expect(wrapper).to.have.length(1);
  });

  it('has only 1 active tab', () => {
    expect(wrapper.find('.active').length).to.be.at.most(1);
  });

});
