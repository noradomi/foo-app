import React from 'react';
import './ToolbarButton.css';

export default function ToolbarButton(props) {
    const { icon } = props;
    return (
      // <a href="/logout"><i className={`toolbar-button ${icon}`} /></a>
      
      <i className={`toolbar-button ${icon}`} />
    );
}