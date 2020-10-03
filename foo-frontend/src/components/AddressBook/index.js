import { Avatar, List, message, Spin, Button } from 'antd';
import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import reqwest from 'reqwest';
import './AddressBook.css';
import { DownloadOutlined } from '@ant-design/icons';

const fakeDataUrl = 'https://randomuser.me/api/?results=20&inc=name,gender,email,nat&noinfo';

class AddressBook extends React.Component {
	state = {
		data: [],
		loading: false,
		hasMore: true
	};

	componentDidMount() {
		this.fetchData((res) => {
			this.setState({
				data: res.results
			});
		});
	}

	fetchData = (callback) => {
		reqwest({
			url: fakeDataUrl,
			type: 'json',
			method: 'get',
			contentType: 'application/json',
			success: (res) => {
				callback(res);
			}
		});
	};

	handleInfiniteOnLoad = () => {
		let { data } = this.state;
		this.setState({
			loading: true
		});
		if (data.length > 50) {
			message.warning('Infinite List loaded all');
			this.setState({
				hasMore: false,
				loading: false
			});
			return;
		}
		this.fetchData((res) => {
			data = data.concat(res.results);
			this.setState({
				data,
				loading: false
			});
		});
	};

	render() {
		return (
			<div className="demo-infinite-container">
				<InfiniteScroll
					initialLoad={false}
					pageStart={0}
					loadMore={this.handleInfiniteOnLoad}
					hasMore={!this.state.loading && this.state.hasMore}
					useWindow={false}
				>
					<List
						dataSource={this.state.data}
						renderItem={(item) => (
							<List.Item
								key={item.id}
								actions={[
									<Button type="primary" shape="circle" icon={<DownloadOutlined />} size={'large'} />
								]}
							>
								<List.Item.Meta
									avatar={
										<Avatar src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png" />
									}
									title={<a href="https://ant.design">{item.name.last}</a>}
									// description={item.email}
								/>
								{/* <div>Content</div> */}
							</List.Item>
						)}
					>
						{this.state.loading &&
						this.state.hasMore && (
							<div className="demo-loading-container">
								<Spin />
							</div>
						)}
					</List>
				</InfiniteScroll>
			</div>
		);
	}
}

export default AddressBook;
