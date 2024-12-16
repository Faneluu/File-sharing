import Navbar from "../../components/Navbar";
import SideBar from "../../components/SideBar";
import DisplayContainer from "../../components/DisplayContainer";
import "../../css/HomeUser.css";

function HomeUser() {
  return (
    <>
      <Navbar />
      <div id="mainCont">
        <SideBar />
        <DisplayContainer />
      </div>
    </>
  );
}

export default HomeUser;
