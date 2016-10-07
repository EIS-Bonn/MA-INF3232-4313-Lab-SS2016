import React, { PropTypes } from 'react';

import Header from '../components/layout/Header';

const Layout = ({location, children}) => {

  return (
      <div>
        <Header location={location} />
        <div class='layout col-lg-12'>
          {children}
        </div>
      </div>
    );

};

Layout.propTypes = {
  location: PropTypes.object.isRequired
};


export default Layout;
