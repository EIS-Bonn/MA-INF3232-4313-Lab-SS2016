import React, { PropTypes } from 'react';

import { IndexLink, Link } from 'react-router';

const Header = ({ location }) => {

  return (
    <nav class='navbar-fixed-top' role='navigation'>
      <div class='navbar-header'>
        <a class='navbar-brand'>RDFJS 4 U</a>
      </div>
      <ul class='nav navbar-right top-nav'>
        <li class={`${location.pathname === '/' ? 'open' : ''}`}>
          <IndexLink to='/'><i class='fa fa-bar-chart-o'></i> Libraries</IndexLink>
        </li>
        <li class={`${location.pathname.match(/^\/documentation/) ? 'open' : ''}`}>
          <Link to='documentation'><i class='fa fa-fw fa-file'></i> Documentation</Link>
        </li>
        <li class={`${location.pathname.match(/^\/about/) ? 'open' : ''}`}>
          <Link to='about'><i class='fa fa-user'></i> About</Link>
        </li>
      </ul>
    </nav>
  );

};

Header.propTypes = {
  location: PropTypes.object.isRequired
};


export default Header;
