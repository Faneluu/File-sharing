import React, { useState } from 'react';
import { IoSearch } from "react-icons/io5";
import { Line } from 'react-chartjs-2';
import { Chart as Chartjs, Title, Tooltip, LineElement, Legend, CategoryScale, LinearScale, PointElement } from 'chart.js';
import { CircularProgressbar } from 'react-circular-progressbar';
import { Card } from 'react-bootstrap';
import 'react-circular-progressbar/dist/styles.css';
import logo from '../../assets/images/logo.png';
import '../../css/HomeAdmin.css';

Chartjs.register(
    Title, Tooltip, LineElement, Legend, CategoryScale, LinearScale, PointElement
);

const HomeAdmin = () => {
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
                borderColor: 'white',
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
                    color: 'white',
                },
                ticks: {
                    padding: 10,
                    color: 'white',
                },
                offset: true,
                grid: {
                    color: 'grey',
                    borderColor: 'white',
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
                    align: 'end',
                    color: 'white',
                },
                ticks: {
                    padding: 10,
                    stepSize: 10,
                    color: 'white',
                },
                offset: true,
                grid: {
                    color: 'grey',
                    borderColor: 'white',
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
                color: 'white',
            },
        },
    };

    return (
        <div>
            <header className="header">
                <div className="logo-container">
                    <img src={logo} alt="Logo" className="logo-image" />
                    <h1 className="logo-text">ATM File Sharing</h1>
                </div>
                <div className="admin-account">ADMIN ACCOUNT</div>
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
            </header>

            <div className="content">
                <div className="charts">
                    <Card className="chart-card">
                        <Card.Body>
                            <Line data={data} options={options} className="used-space-chart" />
                        </Card.Body>
                    </Card>
                </div>

                <div className="progress-container">
                    <Card  className="progress-card">
                        <Card.Body>
                            <div className="progress-bars">
                                <CircularProgressbar value={20} text={`${20}%`} className="progress-bar" />
                                <CircularProgressbar value={90} text={`${90}%`} className="progress-bar" />
                                <CircularProgressbar value={60} text={`${60}%`} className="progress-bar" />
                            </div>
                            <div className="bar-info">
                                Buckets
                            </div>
                        </Card.Body>
                    </Card>

                    <Card className="table-card">
                        <Card.Body>
                            <table className="data-table">
                                <tbody>
                                    <tr>
                                        <td>1</td>
                                        <td>Back-up bucket_1</td>
                                        <td className="download1" onClick={() => handleDownload('backup1.zip')}>10.11.2024</td>
                                    </tr>
                                    <tr>
                                        <td>2</td>
                                        <td>Back-up bucket_2</td>
                                        <td className="download2" onClick={() => handleDownload('backup2.zip')}>1.1.2024</td>
                                    </tr>
                                    <tr>
                                        <td>3</td>
                                        <td>Back-up bucket_3</td>
                                        <td className="download3" onClick={() => handleDownload('backup3.zip')}>2.08.2024</td>
                                    </tr>
                                </tbody>
                            </table>
                        </Card.Body>
                    </Card>
                </div>
            </div>
        </div>
    );
};

export default HomeAdmin;
