import React, {Component, Fragment} from 'react'

class PublicPage extends Component {
    constructor(props) {
        super(props)
    }

    render() {
        return(
            <Fragment>
                <h1>Public page</h1>
                The url is {this.props.match.params.id}
            </Fragment>
        )
    }
}

export default PublicPage;