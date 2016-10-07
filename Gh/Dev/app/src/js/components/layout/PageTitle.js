import React, { PropTypes } from 'react';

const PageTitle = ({ pageTitle }) => {

    return (
      <div class="row">
        <div class="col-lg-12">
          <h1 class="page-header">
            {pageTitle}
          </h1>
        </div>
      </div>
    );

};

PageTitle.propTypes = {
  pageTitle: PropTypes.string.isRequired
};

export default PageTitle;
