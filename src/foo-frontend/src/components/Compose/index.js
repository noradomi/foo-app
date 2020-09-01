import React from 'react';
import './Compose.css';

export default function Compose(props) {
    return (
      <div className="compose">
        <input
          type="text"
          className="compose-input"
          placeholder="  @name"
          style={{float: "left"}}
          onKeyUp={props.onKeyUp}
        />

        {
          props.rightItems
        }
      </div>
    );
}