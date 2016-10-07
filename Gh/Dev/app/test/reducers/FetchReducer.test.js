import { expect } from 'chai';
import FetchReducer from '../../src/js/reducers/reducer_status';

describe('(Reducer) Fetch Status', () => {

  it('should change fetch flag status', () => {

    const stateBefore = () => {
      return { tcontent: false };
    }
    const action = { type: 'GET_TABLE_CONTENT' };

    const actual = FetchReducer(stateBefore, action);
    const expected = { tcontent: true };

    expect(actual).to.deep.equal(expected);
  });

});
