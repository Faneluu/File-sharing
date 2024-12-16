import React, { useEffect, useState } from "react";
import logo from "../assets/images/logo.png";
import search from "../assets/images/search.png";
import profile_icon from "../assets/images/profile_icon.png";
import settings from "../assets/images/settings.png";
import "../css/Navbar.css";
import "../css/AnimatedText.css";
import "../css/DarkModeButton.css";

export default function Navbar() {
  const [isDarkMode, setIsDarkMode] = useState(false);

  useEffect(() => {
    const savedMode = localStorage.getItem("isDarkMode");

    if (savedMode === "false") {
      setIsDarkMode(false);
      document.body.classList.add("light-mode");
      document.body.classList.remove("dark-mode");
    } else {
      setIsDarkMode(true);
      document.body.classList.add("dark-mode");
      document.body.classList.remove("light-mode");
    }
  }, []);

  const toggleDarkMode = () => {
    const newDarkModeValue = !isDarkMode;
    setIsDarkMode(newDarkModeValue);

    localStorage.setItem("isDarkMode", newDarkModeValue);

    const darkModeEvent = new Event("darkModeUpdated");
    window.dispatchEvent(darkModeEvent);

    if (newDarkModeValue) {
      document.body.classList.add("dark-mode");
      document.body.classList.remove("light-mode");
    } else {
      document.body.classList.add("light-mode");
      document.body.classList.remove("dark-mode");
    }
  };

  return (
    <nav>
      <ul>
        <li>
          <a href="/home">
            <div id="icon">
              <img src={logo} alt="ATM File Sharing Logo" />
              <p className="animated-text">ATM File Sharing</p>
            </div>
          </a>
        </li>

        <li>
          <div id="searchBar">
            <button>
              <img src={search} alt="Search" className="opacity" />
            </button>
            <input type="text" placeholder="Search" />
          </div>
        </li>

        <li>
          <div id="navOptions">
            <div className="dark-mode-toggle">
              <div className="wrapper">
                <input
                  type="checkbox"
                  id="hide-checkbox"
                  checked={!isDarkMode} // Light mode = checkbox ON
                  onChange={toggleDarkMode}
                />
                <label htmlFor="hide-checkbox" className="toggle">
                  <span className="toggle-button">
                    {/* Craterele */}
                    <div className="crater crater-1"></div>
                    <div className="crater crater-2"></div>
                    <div className="crater crater-3"></div>
                    <div className="crater crater-4"></div>
                    <div className="crater crater-5"></div>
                    <div className="crater crater-6"></div>
                    <div className="crater crater-7"></div>
                  </span>
                  {/* Stelele animate */}
                  <span className="star star-1"></span>
                  <span className="star star-2"></span>
                  <span className="star star-3"></span>
                  <span className="star star-4"></span>
                  <span className="star star-5"></span>
                  <span className="star star-6"></span>
                  <span className="star star-7"></span>
                  <span className="star star-8"></span>
                </label>
              </div>
            </div>
            <button>
              <img src={profile_icon} alt="Profile_Icon" className="opacity" />
            </button>
            <button>
              <img src={settings} alt="Settings" className="opacity" />
            </button>
          </div>
        </li>
      </ul>
    </nav>
  );
}
