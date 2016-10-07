import { combineReducers } from 'redux';
import LibTableReducer from './reducer_libtable';
import FeaturesReducer from './reducer_features';
import FetchReducer from './reducer_status';

const rootReducer = combineReducers({
  features: FeaturesReducer,
  fetchstatus: FetchReducer,
  tabledata: LibTableReducer,
});

export default rootReducer;
