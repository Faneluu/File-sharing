import React, { useState } from "react";
import Cookies from "js-cookie";
import { Container, Row, Col, Nav, Tab, Form, Button, Alert } from "react-bootstrap";
import logo from "../../assets/images/logo.png";
import "bootstrap/dist/css/bootstrap.min.css";
import { useNavigate } from "react-router-dom";

import API from "../../components/AxiosConfig";

function LoginRegister() {
  const [activeTab, setActiveTab] = useState("login");

  const [loginData, setLoginData] = useState({ username: "", password: "" });
  const [loginError, setLoginError] = useState("");

  const [registerData, setRegisterData] = useState({
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
  });
  const [registerError, setRegisterError] = useState("");

  const navigate=useNavigate();
  const handleLogin = async () => {
    const { username, password } = loginData;
    if (!username || !password) {
      setLoginError("Please fill in all fields.");
      return;
    }

    try {
      const response = await API.post("/user/login", { username, password });

      const token = response.headers["authorization"]; 
      const headers = response.headers;
      console.log(headers);
      console.log(token);

      if (token) {
        const decodedToken = decodeURIComponent(token.split("Bearer ")[1]); 
        Cookies.set("JWT", decodedToken, { expires: 7, secure: true, sameSite: "Strict" }); 
      }

      setLoginError("");
      console.log("Login successful");
      navigate("/home");
    } catch (error) {
      setLoginError(error.response?.data || "Login failed");
    }
  };

  const handleRegister = async () => {
    const { username, email, password, confirmPassword } = registerData;

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!username || !email || !password || !confirmPassword) {
      setRegisterError("Please fill in all fields.");
      return;
    } else if (!emailRegex.test(email)) {
      setRegisterError("Please enter a valid email address.");
      return;
    } else if (password !== confirmPassword) {
      setRegisterError("Passwords do not match.");
      return;
    }

    try {
      const response = await API.post("/user/register", { username, email, password });
      setRegisterError("");
      console.log(response.data);
    } catch (error) {
      setRegisterError(error.response?.data || "Registration failed");
    }
  };

  return (
    <Container>
      <Row className="justify-content-center mb-4">
        <img src={logo} alt="Logo" className="img-fluid" style={{ maxWidth: "300px", maxHeight: "300px" }} />
      </Row>
      <Row className="justify-content-center mb-4">
        <h1 className="text-center">File Sharing</h1>
      </Row>
      <Row className="justify-content-center">
        <Col md={6} className="shadow p-4 bg-white rounded">
          <Tab.Container activeKey={activeTab} onSelect={(k) => setActiveTab(k)}>
            <Nav variant="tabs" className="justify-content-center mb-4">
              <Nav.Item>
                <Nav.Link eventKey="login">Log in</Nav.Link>
              </Nav.Item>
              <Nav.Item>
                <Nav.Link eventKey="register">Sign up</Nav.Link>
              </Nav.Item>
            </Nav>
            <Tab.Content>
              <Tab.Pane eventKey="login">
                <Form>
                  <Form.Group controlId="formUsername" className="mb-3">
                    <Form.Label className="text-dark">Username</Form.Label>
                    <Form.Control
                      type="text"
                      placeholder="Enter your username"
                      value={loginData.username}
                      onChange={(e) =>
                        setLoginData({ ...loginData, username: e.target.value })
                      }
                    />
                  </Form.Group>
                  <Form.Group controlId="formPassword" className="mb-3">
                    <Form.Label className="text-dark">Password</Form.Label>
                    <Form.Control
                      type="password"
                      placeholder="Enter your password"
                      value={loginData.password}
                      onChange={(e) =>
                        setLoginData({ ...loginData, password: e.target.value })
                      }
                    />
                  </Form.Group>
                  {loginError && <Alert variant="danger">{loginError}</Alert>}
                  <Button variant="primary" className="w-100" onClick={handleLogin}>
                    Log in
                  </Button>
                </Form>
              </Tab.Pane>

              <Tab.Pane eventKey="register">
                <Form>
                  <Form.Group controlId="formUsername" className="mb-3">
                    <Form.Label className="text-dark">Username</Form.Label>
                    <Form.Control
                      type="text"
                      placeholder="Enter your username"
                      value={registerData.username}
                      onChange={(e) =>
                        setRegisterData({ ...registerData, username: e.target.value })
                      }
                    />
                  </Form.Group>
                  <Form.Group controlId="formEmail" className="mb-3">
                    <Form.Label className="text-dark">Email</Form.Label>
                    <Form.Control
                      type="email"
                      placeholder="Enter your email"
                      value={registerData.email}
                      onChange={(e) =>
                        setRegisterData({ ...registerData, email: e.target.value })
                      }
                    />
                  </Form.Group>
                  <Form.Group controlId="formPassword" className="mb-3">
                    <Form.Label className="text-dark">Password</Form.Label>
                    <Form.Control
                      type="password"
                      placeholder="Create a password"
                      value={registerData.password}
                      onChange={(e) =>
                        setRegisterData({ ...registerData, password: e.target.value })
                      }
                    />
                  </Form.Group>
                  <Form.Group controlId="formConfirmPassword" className="mb-3">
                    <Form.Control
                      type="password"
                      placeholder="Repeat password"
                      value={registerData.confirmPassword}
                      onChange={(e) =>
                        setRegisterData({
                          ...registerData,
                          confirmPassword: e.target.value,
                        })
                      }
                    />
                  </Form.Group>
                  {registerError && <Alert variant="danger">{registerError}</Alert>}
                  <Button variant="success" className="w-100" onClick={handleRegister}>
                    Create account
                  </Button>
                </Form>
              </Tab.Pane>
            </Tab.Content>
          </Tab.Container>
        </Col>
      </Row>
    </Container>
  );
}

export default LoginRegister;
