import React from 'react';
import { expect } from 'chai';
import { shallow, mount, render } from 'enzyme';

import SearchResult from '../../src/js/components/table/SearchResult';

describe("(Component) SearchResult", function() {

  it('renders without exploding', () => {
    const wrapper = shallow(<SearchResult results={2} />);

    expect(wrapper).to.have.length(1);
  });

});
