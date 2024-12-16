import React, { useState, useRef } from "react";
import API from "./AxiosConfig";
import new_file from "../assets/images/drive_icon.png";
import { Alert } from "react-bootstrap";

function FileUploadButton() {
  const [alert, setAlert] = useState(null);
  const fileInputRef = useRef();

  const handleButtonClick = () => {
    fileInputRef.current.click();
  };

  const handleFileChange = async (event) => {
    const file = event.target.files[0];
    console.log(file);
    if (file) {
      const formData = new FormData();
      formData.append("file", file);

      try {
        const response = await API.post(
          `/files/${file.name}`,
          formData
        );
        console.log("Upload successful:", response.data);
        setAlert({ variant: "secondary", message: "Upload successful!" });
        setTimeout(() => {
          setAlert(null);
        }, 3000);
      } catch (error) {
        console.error("Error uploading file:", error);
        setAlert({ variant: "danger", message: "Error uploading file." });
        setTimeout(() => {
          setAlert(null);
        }, 3000);
      }
    }
  };

  return (
    <>
      {alert && (
        <Alert variant={alert.variant}>
          {alert.message}
        </Alert>
      )}
      <button id="linkBtn" onClick={handleButtonClick}>
        <img src={new_file} alt="New" />
        <p>New</p>
      </button>
      <input
        type="file"
        ref={fileInputRef}
        style={{ display: "none" }}
        onChange={handleFileChange}
      />
    </>
  );
}

export default FileUploadButton;
