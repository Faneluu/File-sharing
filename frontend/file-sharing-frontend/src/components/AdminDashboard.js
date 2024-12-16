import React, { useState } from 'react';
import { IoSearch } from "react-icons/io5";
import { Line } from 'react-chartjs-2';
import { Chart as Chartjs, Title, Tooltip, LineElement, Legend, CategoryScale, LinearScale, PointElement } from 'chart.js';
import { CircularProgressbar } from 'react-circular-progressbar';
import 'react-circular-progressbar/dist/styles.css';

import logo from '../assets/Logo.png';
import '../../src/assets/AdminDashboard.css';

Chartjs.register(
  Title, Tooltip, LineElement, Legend, CategoryScale, LinearScale, PointElement
);

const AdminDashboard = () => {
  const [searchItem, setSearchTerm] = useState('');

  const handleChange = (e) => {
    setSearchTerm(e.target.value);
  };

  const handleDownload = (filename) => {
    const element = document.createElement("a");
    element.href = `/path/to/files/${filename}`; 
    element.download = filename;
    document.body.appendChild(element);
    element.click();
    document.body.removeChild(element);
  };

  const data = {
    labels: ['13.11.2024', '14.11.2024', '15.11.2024', '16.11.2024'],
    datasets: [
      {
        data: [10, 45, 20, 30],
        borderColor: 'black',
        backgroundColor: 'black',
      },
    ],
  };

  const options = {
    responsive: true,
    scales: {
      x: {
        title: {
          display: true,
          text: 'Days',
          align: 'end',
        },
        ticks: {
          padding: 10,
        },
        offset: true,
        grid: {
          color: '#d3d3d3',
          borderColor: '#d3d3d3',
          borderWidth: 2,
          lineWidth: 2,
          drawOnChartArea: true,
          drawTicks: true,
        },
      },
      y: {
        title: {
          display: true,
          text: 'GB',
          position: 'top',
          align: 'end'
        },
        ticks: {
          padding: 10,
          stepSize: 10,
        },
        offset: true,
        grid: {
          color: '#d3d3d3',
          borderColor: '#d3d3d3',
          borderWidth: 2,
          lineWidth: 2,
          drawOnChartArea: true,
          drawTicks: true,
        },
      },
    },
    plugins: {
      legend: {
        display: false,
      },
      title: {
        display: true,
        text: 'Used space',
        position: 'bottom',
      },
    },
  };

  return (
    <div>
      <div className='menu-bar'>
        <div className='logo-container'>
          <img src={logo} alt="Logo" className="logo-image" />
          <h1 className='logo-text'>ATM File Sharing</h1>
          <h1 className='admin-account'>ADMIN ACCOUNT</h1>
          <div className="search-container">
            <IoSearch className="search-icon" />
            <input
              className="search-bar"
              onChange={handleChange}
              type="search"
              placeholder="Search..."
              value={searchItem}
            />
          </div>
        </div>
      </div>
      <div className='container-details'>
        <div className="charts-container">
          <Line data={data} options={options} className='used-space-chart' />
        </div>
        <div className='right-container'>
          <div className='progress-bars-container'>
            <div className='bar'>
              <CircularProgressbar value={20} text={`${20}%`} />
            </div>
            <div className='bar'>
              <CircularProgressbar value={90} text={`${90}%`} />
              <div className='bar-info'>
                Buckets
              </div>
            </div>
            <div className='bar'>
              <CircularProgressbar value={60} text={`${60}%`} />
            </div>
          </div>
          <div className="table-container">
            <table className="data-table">
              <thead>
                <tr>
                  <td>1</td>
                  <td>Back-up bucket_1</td>
                  <td className='download' style={{ backgroundColor: 'green' }} onClick={() => handleDownload('backup1.zip')}>10.11.2024</td>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>2</td>
                  <td>Back-up bucket_2</td>
                  <td className='download' style={{ backgroundColor: 'red' }} onClick={() => handleDownload('backup2.zip')}>1.1.2024</td>
                </tr>
                <tr>
                  <td>3</td>
                  <td>Back-up bucket_3</td>
                  <td className='download' style={{ backgroundColor: 'orange' }} onClick={() => handleDownload('backup3.zip')}>2.08.2024</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
