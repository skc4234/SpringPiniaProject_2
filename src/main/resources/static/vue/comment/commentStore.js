const {defineStore}=Pinia

const initialState=()=>({
	rList:[],
	page:1,
	fno:0,
	count:0,
	totalpage:0
})
const userCommentStore=defineStore('comment_list',{
	state:initialState,
	actions:{
		async commentListData(){
			const res=await api.get('/comment/list_vue',{
				params:{
					page:this.page,
					fno:this.fno
				}
			})
			console.log(res.data)
		}
	}
})