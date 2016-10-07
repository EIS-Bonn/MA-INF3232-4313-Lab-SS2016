import * as actions from '../actions/index';

export default function(state = {tabs: false, features: false, theaders: false, tcontent: false}, action) {
  switch (action.type) {
    case actions.GET_FEATURE_SETS:
      return {...state, features: true};
    case actions.GET_TABLE_HEADER:
      return {...state, theaders: true};
    case actions.GET_TABLE_TABS:
      return {...state, tabs: true};
    case actions.GET_TABLE_CONTENT:
      return {...state, tcontent: true};
  }

  return state;
}
