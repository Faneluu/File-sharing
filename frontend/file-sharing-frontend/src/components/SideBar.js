import React, { useState, useEffect } from "react";
import shared from "../assets/images/shared.png";
import recent from "../assets/images/recent.png";
import starred from "../assets/images/starred.png";
import trash from "../assets/images/trash.png";
import cloud from "../assets/images/cloud.png";
import "../css/SideBar.css";
import files from "../assets/images/folder.png";
import FileUploadButton from "./FileUploadButton";

export default function SideBar() {
  const [activeButton, setActiveButton] = useState(null);
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

  const menuOptions = [
    { id: "My Files", icon: files, label: "My Files" },
    { id: "Shared with me", icon: shared, label: "Shared with me" },
    { id: "Recent", icon: recent, label: "Recent" },
    { id: "Bookmarks", icon: starred, label: "Bookmarks" },
    { id: "Trash", icon: trash, label: "Trash" },
  ];

  return (
    <div id="sideBar">
      {/* <button id="linkBtn">
        <img src={new_file} alt="New" />
        <p>New</p>
      </button> */}
      <FileUploadButton />

      <div id="sideBarOpt">
        {menuOptions.map((option) => (
          <button
            key={option.id}
            className={`sideBarOptions ${activeButton === option.id ? "activeSideOpt" : ""
              }`}
            onClick={() => setActiveButton(option.id)}
          >
            <img
              src={option.icon}
              alt={option.label}
              className={`opacity ${isDarkMode ? 'dark-icon' : 'light-icon'}`}
            />
            <h3>{option.label}</h3>
          </button>
        ))}
      </div>

      <div id="storageInfo">
        <button
          className={`sideBarOptions ${activeButton === "Storage" ? "activeSideOpt" : ""
            }`}
          onClick={() => setActiveButton("Storage")}
        >
          <img
            src={cloud}
            alt="Storage"
            className={`opacity ${isDarkMode ? 'dark-icon' : 'light-icon'}`}
          />
          <h3>Storage</h3>
        </button>

        <div className="sideBarOptions">
          <div id="storageLoader">
            <div id="preLoader"></div>
          </div>
        </div>

        <div id="storageNumericalInfo">
          <p>190 GB of 300 GB Used</p>
        </div>
      </div>
    </div>
  );
}
