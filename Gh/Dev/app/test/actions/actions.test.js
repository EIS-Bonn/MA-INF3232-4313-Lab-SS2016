import { expect } from 'chai';
import * as actions from '../../src/js/actions/index';

describe('(Actions) Action creators', () => {
  it('should create an action to get table tabs', () => {
    expect(actions.getTableTabs()).to.have.length(1);
  })
})
