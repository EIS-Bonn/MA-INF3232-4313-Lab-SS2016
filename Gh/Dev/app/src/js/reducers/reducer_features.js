import { GET_FEATURE_SETS } from '../actions/index';

export default function(state = [], action) {
  switch (action.type) {
    case GET_FEATURE_SETS:
      return action.payload;
  }

  return state;
}
