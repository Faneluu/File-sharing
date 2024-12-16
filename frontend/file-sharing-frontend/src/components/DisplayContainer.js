import React, { useEffect, useState } from "react";
import list_view from "../assets/images/list_view.jpg";
import info from "../assets/images/info.png";
import "../css/DisplayContainer.css";
import DisplayCard from "./DisplayCard";

export default function DisplayContainer() {
  const [isDarkMode, setIsDarkMode] = useState(false);

  useEffect(() => {
    const savedMode = localStorage.getItem("isDarkMode");
    setIsDarkMode(savedMode === "true");
  }, []);

  return (
    <>
      <div id="displayCont">
        <div id="displayInfoNav">
          <button>
            <img
              src={list_view}
              alt="List View"
              className={`opacity ${isDarkMode ? "dark-icon" : "light-icon"}`}
            />
          </button>

          <button>
            <img
              src={info}
              alt="Info"
              className={`opacity ${isDarkMode ? "dark-icon" : "light-icon"}`}
            />
          </button>
        </div>

        <div id="contentDisplayer">
          <DisplayCard />
          <DisplayCard />
          <DisplayCard />
          <DisplayCard />
          <DisplayCard />
          <DisplayCard />
        </div>
      </div>
    </>
  );
}
