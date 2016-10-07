import * as actions from '../actions/index';

const INITIAL_STATE = {tabs: [], headers: [], content: []};

export default function(state = INITIAL_STATE, action) {
  switch (action.type) {
    case actions.GET_TABLE_TABS:
      return {...state, tabs: action.payload};
    case actions.GET_TABLE_HEADER:
      return {...state, headers: action.payload};
    case actions.GET_TABLE_CONTENT:
      return {...state, content: action.payload};
    case actions.QUERY_TABLE:
      return {...state, content: action.payload};
  }

  return state;
}
