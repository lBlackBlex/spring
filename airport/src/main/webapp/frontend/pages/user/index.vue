<template>
  <section class="section">
    <div class="columns is-mobile">
      <UserForm
        class="column"
        :user="user"
        :showButton="true"/>
    </div>
  </section>
</template>

<script>
import UserForm from "@/components/UserForm";

export default {
middleware: ['check-auth', 'auth'],
  components: [
    UserForm
  ],
  data(){
    return{
      user: {}
    }
  },
  created(){
    this.getUser()
  },
  methods: {
    getUser(){
      const userEmail = this.$store.getters.getUserEmail
      const allUsers = this.$store.getters.users
      this.user = allUsers.find(user => user.email === userEmail)
    }
  }
}
</script>
