import React,{Component} from 'react';
import UserDetails from './UserDetails';

class HomePage extends Component {
    constructor(props) {
        super(props)
    }
    render() {
        return (
            <div>
                <h1>Hello {this.props.token}</h1>
                 {/* <UserDetails token={this.props.token}></UserDetails>  */}
            </div>
        )
    }
}

export default HomePage;