import React from 'react';
import {CaretDownIcon as CaretDown} from '@phosphor-icons/react';

/** Preserves native select behavior with a consistent, non-interactive caret. */
export default function SelectControl({children, ...props}) {
 return <span className="select-control"><select {...props}>{children}</select><CaretDown size={14} aria-hidden="true"/></span>;
}
