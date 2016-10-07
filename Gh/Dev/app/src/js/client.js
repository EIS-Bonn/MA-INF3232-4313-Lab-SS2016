import React from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, IndexRoute, hashHistory } from 'react-router';
import { Provider } from 'react-redux';
import { createStore, applyMiddleware } from 'redux';
import reduxThunk from 'redux-thunk';
import createLogger from 'redux-logger';

import About from './pages/About';
import Documentation from './pages/Documentation';
import Layout from './pages/Layout';
import Libraries from './pages/Libraries';
import reducers from './reducers';

const createStoreWithMiddleware = applyMiddleware(reduxThunk)(createStore);

const app = document.getElementById('app');

const provider = (
  <Provider store={createStoreWithMiddleware(reducers)}>
    <Router history={hashHistory}>
      <Route path='/' component={Layout}>
        <IndexRoute component={Libraries}></IndexRoute>
        <Route path='documentation' name="documentation" component={Documentation}></Route>
        <Route path='about' name="about" component={About}></Route>
      </Route>
    </Router>
  </Provider>
);

ReactDOM.render(provider, app);
