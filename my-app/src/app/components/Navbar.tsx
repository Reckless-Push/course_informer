import React, { Component } from "react";
import styles from "@/app/components/css/navbar.module.css";

class Navbar extends Component {
    render() {
        return (
            <nav className={styles.nav}>
                <div>
                    <ul>
                        <li><a>UMass Amherst</a></li>
                        <li><a>My Profile</a></li>
                        <li><a>Logout</a></li>
                    </ul>
                </div>

            </nav>
        );
    }
}

export default Navbar;