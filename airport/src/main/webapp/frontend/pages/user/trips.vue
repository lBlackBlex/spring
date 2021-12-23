<template>
  <section class="section">
    <div class="columns is-mobile">
      <div
        class="columns is-mobile" v-for="item in userTickets">
        <Ticket :ticket="item"/>
      </div>
    </div>
  </section>
</template>

<script>
import Ticket from "@/components/Ticket";

export default {
  middleware: ['check-auth', 'auth'],
  components: [
    Ticket
  ],
  data(){
    return{
      userTickets: {}
    }
  },
  created(){
    this.getUserTickets()
  },
  methods: {
    async getUserTickets(){
      const userEmail = this.$store.getters.getUserEmail
      const allUsers = this.$store.getters.users
      const user = allUsers.find(user => user.email === userEmail)
      const tickets = await this.$axios.$get('http://localhost:8080/api/v1/tickets/findByUser', {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `${this.$store.getters.getToken}`
        },
        params: {
          'userId': `${user.id}`
        }
      })
      this.userTickets = tickets
      console.log(tickets)

    }
  }
}
</script>
