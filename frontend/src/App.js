import React,{Component} from 'react';
import logo from './logo.svg';
import './App.css';
import RegisterPage from './PollApp/RegisterPage';
import LoginPage from './PollApp/LoginPage';
import HomePage from './PollApp/HomePage';

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      userToken : ''
    }
  }

  setUserToken = (token) => {
    this.setState({
      userToken : token
    })
  }

   render() {

  if(this.state.userToken.length==0 ) {
    return(
      <React.Fragment>
        <RegisterPage></RegisterPage>
        <LoginPage setMethod={this.setUserToken}></LoginPage>
      </React.Fragment>
    )

  } else {
    return(
      <React.Fragment>
        <HomePage token={this.state.userToken}></HomePage>
        <h3>{this.state.userToken.length}</h3>
        <h3>{this.state.userToken}</h3>
      </React.Fragment>

    )

  }
  // return (

  //   <div className="App">
  //     {/* <header className="App-header">
  //       <img src={logo} className="App-logo" alt="logo" />
  //       <p>
  //         Edit <code>src/App.js</code> and save to reload.
  //       </p>
  //       <a
  //         className="App-link"
  //         href="https://reactjs.org"
  //         target="_blank"
  //         rel="noopener noreferrer"
  //       >
  //         Learn React
  //       </a>
  //     </header> */}
      
  //     <RegisterPage></RegisterPage>
  //     <LoginPage setMethod={this.setUserToken}></LoginPage>
  //     <HomePage name={this.userToken}></HomePage>
  //     <h3>{this.state.userToken.length}</h3>
  //     <h3>{this.state.userToken}</h3>
  //   </div>
  // );

    }
}

export default App;
