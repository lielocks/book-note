## Naver open API 를 통한 책 검색 및 저장, 외부 공유가 가능하고 저장된 책에 대한 노트 작성 및 해당 노트 공유 서비스

http://ec2-13-124-143-107.ap-northeast-2.compute.amazonaws.com:8088/swagger-ui/index.html 

에서 Swagger API 명세서를 확인하실 수 있습니다.

--- 

-PostgreSQL 15 (docker image port 5432)

-Redis (docker image port 6379)

-Spring Boot 3.3.3

-KAKAO OAuth2

-GraphQL Schema First 

-Dockerize

-Github Action CI/CD

-Naver 책검색 Open API

-JWT 토큰을 사용해 저장한 책 정보를 외부 공유할 수 있는 기능
- 공유를 통해 타 사용자/비로그인 사용자는 공유자가 저장한 책 정보 및 해당 책에 공유자가 작성한 노트를 열람할 수 있습니다.
- 열람 만료 기한은 공유 시점으로부터 10분 후

-노트 삭제 soft delete, hard delete 

<br>


### API 호출

**GraphQL path :**

http://ec2-13-124-143-107.ap-northeast-2.compute.amazonaws.com:8088/graphiql?path=/graphql

<br>

![image](https://github.com/user-attachments/assets/0692a00b-0e28-4975-b1c1-06eb7d44873b)

*모든 mutation 과 query 실행 전 `Authorization header` 에 access token 설정해주세요*

<br>


> mutation {  
>   fetchAndSaveBook(isbn: "9791161752624") {  
>     id  
>     title  
>     link  
>     author  
>     discount  
>     publisher  
>     pubdate  
>     isbn  
>     description  
>   }  
> }  

> mutation {  
>   userLikeBook(UserLikeBookInput: { userId: 1, bookId: 1 })  
> }  

> mutation {  
>   registerNote(NoteRegisterInput: { bookId: 2, content: "2번 책 의 3번 노트" }) {  
>     noteId  
>     content  
>     isDeleted  
>     createdAt  
>     updatedAt  
>     bookId  
>   }  
> }  

> mutation {  
>   updateNote(NoteUpdateInput: { noteId: 1, content: "1번 책의 1번 노트!" }) {  
>     noteId  
>     content  
>     isDeleted  
>     createdAt  
>     updatedAt  
>     bookId  
>   }  
> }  

> mutation {  
>  softDeleteNote(NoteSoftDeleteDto: {  
>    userId: 1,  
>    noteId: 2,  
>    deleted: true  
>  }) {  
>    noteId  
>    content  
>    isDeleted  
>    createdAt  
>    updatedAt  
>    bookId  
>  }  
> }    

> mutation {  
>   hardDeleteNote(noteId: 2)  
> }  

> query {  
>   getNoteListByUserId {  
>     noteId  
>     content  
>     isDeleted  
>     createdAt  
>     updatedAt  
>     bookId  
>   }  
> }

> query {  
> getUserBookList {  
> id  
> title  
> link  
> author  
> discount  
> publisher  
> pubdate  
> isbn  
> description  
> }  
> }  
<br>

**Rest API**

https://www.postman.com/aerospace-administrator-44708254/works/collection/86kk4ot/book-note?action=share&creator=22572966
