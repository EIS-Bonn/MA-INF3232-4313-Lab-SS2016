import React, { PropTypes } from 'react';

const Checkbox = ({value, title, onChange}) => {

    return (
      <div class='filter'>
        <a class="checkbox">
          <label>
            <input type="checkbox"
                   checked={value}
                   onChange={e => onChange(!value)} />
                   {title}
          </label>
        </a>
      </div>
    );

};

Checkbox.propTypes = {
  value: PropTypes.bool.isRequired,
  title: PropTypes.string.isRequired,
  onChange: PropTypes.func
};

export default Checkbox;
