const {defineStore}=Pinia
// store : 저장 공간 = (처리함수, 데이터)
// 전역공간 => 모든 HTML에서 사용 가능
// 데이터가 변경 => 자동으로 HTML 갱신
// state에 있는 변수만 변경 => data(){return {}}
/*
	1. state : HTML에 적용하는 변수
	2. actions : 사용자 요청 처리 => springBoot와 연동
	3. getters : computed(변경사항 없는 가공된 데이터) => range / 1,000
	
	
	Pinia Store
	--------------------
	state
		- list:[]
		- curpage:0,
		- ...
	getters // 없는 경우도 있음
		- range
	actions
		- foodListData()
		- move()
	---------------------
	==> this
	
	range:(state)
		- state만 사용

	getters는 state에 있는 값만 가공된 값으로 반환
*/
const initialState=()=>({
	list:[],
	curapge:1,
	totalpage:0,
	startpage:0,
	endpage:0,
	no:0,
	detail:{},
})
const userFoodStore=defineStore('food_list',{
	state:initialState,
	// computed
	getters:{ // 공통 사용 함수
		range:(state)=>{
			const arr=[]
			for(let i=state.startpage;i<=state.endpage; i++){
				arr.push(i) // 맨 뒤에 값을 저장
				// pop() // 뒤에서부터 제거
			}
			return arr
		}
	},
	// 기능 => 사용자 요청 => 서버 연동
	actions:{
		async foodListData(){
			const res=await api.get('/food/list_vue',{
				params:{
					page:this.curpage
				}
			})
			console.log(res.data)
			this.list=res.data.list
			this.curpage=res.data.curpage
			this.totalpage=res.data.totalpage
			this.startpage=res.data.startpage
			this.endpage=res.data.endpage
		},
		move(page){
			this.curpage=page
			this.foodListData()
		}
	}
})