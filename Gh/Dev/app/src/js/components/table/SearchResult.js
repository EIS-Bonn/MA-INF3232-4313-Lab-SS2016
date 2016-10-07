import React, { PropTypes } from 'react';

const SearchResult = ({isVisible, results, hideSearch}) => {

  const hide = () => {
    hideSearch();
  };

  let label;

  if(!results ) label = 'Opps! Sorry, but no libraries matched your requrest!';
  else if(results === 1) label = `${results} result has matched your requrest!`;
  else label = `${results} results have matched your requrest!`;

  return (
    <div class="row">
      <div class="col-lg-12">
        <div class={`${isVisible ? '' : 'hide'} alert-info alert`}>
          <button type="button" class="close" onClick={hide}>×</button>
          <i class="fa fa-search"></i> <strong>Search:</strong> {label}
        </div>
      </div>
    </div>
  );

};


SearchResult.propTypes = {
  isVisible: PropTypes.bool.isRequired,
  results: PropTypes.number.isRequired,
  hideSearch: PropTypes.func
};

export default SearchResult;
