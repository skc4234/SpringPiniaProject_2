/*
	Pinia
	 1) 동작 순서
	 	- App 생성(Vue 생성) : createApp()
		- Pinia 등록 : defineStore
		- store 생성
			- state: HTML 코드를 자동으로 갱신하는 변수
			- getters: 실제 계산값(state 변경없이)
			- actions: 실제 서버와 연결 => state 갱신
	 -------------------------------------------
	 2) 사용자가 이벤트를 발생했을때(버튼 클릭, 마우스 클릭)
	 	- store에 있는 action 함수 호출		
		- 서버 연결(axios/fetch)
		- 서버에서 요청 처리 결과값 읽기
		- store에 있는 state 변수 변경(update)
		- HTML에 적용
		Vue   - Vuex  - *Pinia         - Nust
		React - Redux - *tanStackQuery - *Next
		JSP   - MVC   - *ThymeLeaf     - *Spring
		===> JavaScript / Java / Security / JWT / WebSocket(Stomp)
	 
*/
const {defineStore}=Pinia
// defineStore => 새로운 store 생성 시 사용

/*const initialState=()=>({
	rList:[],
	page:1,
	fno:0,
	count:0,
	totalpage:0
})*/
const userCommentStore=defineStore('comment',{
	// HTML에 적용 => 전체 컴포넌트가 사용이 가능하게 변수 설정
	// state 변수는 자바의 static 변수 => 공통 사용 변수
	state:()=>({
		rList:[],
		count:0,
		curpage:1,
		totalpage:0,
		sessionId:'',
		fno:0,
		msg:'',
		upReplyNo:null,
		updateMsg:{}
	}),
	getters:{ // 생략 가능
		
	},
	actions:{
		async commentListData(fno){
			this.fno=fno
			const res=await api.get('/comment/list_vue',{
				params:{
					page:this.curpage,
					fno:this.fno
				}
			})
			console.log(res.data)
			this.rList=res.data.rList
			this.count=res.data.count
			this.curpage=res.data.curpage
			this.totalpage=res.data.totalpage
		},
		async commentInsert(msgRef){
			//this.msg=msgRef
			if(this.msg===''){
				msgRef?.focus()
				return
			}
			const res=await api.post('/comment/insert_vue',{
				page:this.curpage,
				fno:this.fno,
				msg:this.msg
			})
			console.log(res.data)
			this.rList=res.data.rList
			this.count=res.data.count
			this.curpage=res.data.curpage
			this.totalpage=res.data.totalpage
			this.msg=''
		},
		move(page){
			this.curpage=page
			this.commentListData(this.fno)
		},
		async commentDelete(no){
			const res=await api.delete('/comment/delete_vue',{
				//data:{
				params:{
					no:no,
					page:this.curpage,
					fno:this.fno
				}
			})
			console.log(res.data)
			this.rList=res.data.rList
			this.count=res.data.count
			this.curpage=res.data.curpage
			this.totalpage=res.data.totalpage
		},
		async commentUpdate(no){
			const res=await api.put('/comment/update_vue',{
				no:no,
				page:this.curpage,
				fno:this.fno,
				msg:this.updateMsg[no]
			})
			console.log(res.data)
			this.rList=res.data.rList
			this.count=res.data.count
			this.curpage=res.data.curpage
			this.totalpage=res.data.totalpage
			this.upReplyNo=null
		},
		toggleReply(no,msg){
			this.upReplyNo=this.upReplyNo===no?null:no
			this.updateMsg[no]=msg
		}
	}
})