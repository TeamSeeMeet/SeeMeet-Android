# SeeMeet-Android README
그 해, 안드는✨🍒

## ❤️ 약속부터 만남까지, 더 가까운 우리 사이 SeeMeet ❤️

* 지인들과의 **약속**부터, 만남과 **이벤트**를 **관리하는 서비스, 더 나아가서는 인적 네트워크 전체를 관리할 수 있는 서비스를 지향합니다.**
* 👉🏻초대장을 만들어서 가능한 시간을 공유하고, 내 일정과 비교하기 쉽게 도와줘요.
  👉🏻 친구와의 만남과 이벤트를 한 눈에 볼 수 있어요.



##  📝 맡은 역할 (임시)

 <table>
	<tr>
    	<td> 김현아 </td> 
        <td> 친구 관리, 추가, 약속 내역, 약속 상세  </td>
    </tr> 
    <tr>
    	<td> 이동기 </td> 
        <td> 기본 뼈대, 캘린더, 약속 신청, 약속 상세</td>
    </tr>
    <tr>
    	<td> 이유정 </td> 
        <td> 메인, 약속 내역, 받은 요청, 약속 상세 </td>
    </tr>
    <tr>
    	<td> 최유림 </td> 
        <td> 로그인/회원가입, 약속 신청, 보낸 요청, 약속 상세 </td>
    </tr>
 </table>

 </table>

## 👋 Specification   

<table class="tg">
<tbody>
  <tr>
    <td><b>Architecture</b></td>
    <td>MVVM</td>
  </tr>
<tr>
    <td><b>Jetpack Components</b></td>
<td>DataBinding, LiveData, ViewModel, Lifecycle, viewPager2 </td>
</tr>
<tr>
    <td><b>Network</b></td>
<td>OkHttp, Retrofit2</td>
</tr>
<tr>
    <td><b>Strategy</b></td>
<td>Git Flow</td>
</tr>
<tr>
    <td><b>Other Tool</b></td>
<td>Notion, Slack</td>
</tr>

</tbody>
</table>

## 📦 Package Structure폴더링
```bash
* 📦SeeMeet
      └─seemeet
          ├─📂data
          │  └─📂local
          ├─📂ui
          │  ├─📂apply
          │   │  └─📂adapter
          │  ├─📂friend
          │   │  └─📂adapter
          │  ├─📂main
          │   │  └─📂adapter
          │  ├─📂notification
          │   │  └─📂adapter
          │  ├─📂registration
          │  └─📂viewModel
          └─📂util
```

