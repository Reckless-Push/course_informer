import React from 'react'
import styles from "./css/searchBox.module.css";

const SearchBox = () => {
  return (
    <div className={styles.search_container}>
        <input className={styles.searchField} placeholder='search'></input>
        <button className={styles.search_button} aria-label="Search">
      &#128269;</button>
    </div>
  )
}

export default SearchBox;
