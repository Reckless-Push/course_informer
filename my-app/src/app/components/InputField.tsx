import React, {ChangeEvent, FC, InputHTMLAttributes, useState} from 'react'
import styles from "./css/inputField.module.css";

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
    type: 'text' | 'password' | 'email'
    placeholder: string
    value: string | number
    onChange: (e: ChangeEvent<HTMLInputElement>) => void
}

const InputField: FC<InputProps> = ({ type, placeholder, value, onChange, ...otherProps }) => {
    return (
        <div>
            <input type={type} placeholder={placeholder} value={value} onChange={onChange} className={styles.inputField}/>
        </div>
    );
};

export default InputField;