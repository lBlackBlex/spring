<template>
  <section class="section">
    <div class="columns is-mobile">
      <b-field class="column" label="Ticket" label-position="on-border">
        <b-input v-model="ticketId"></b-input>
      </b-field>
      <div class="column buttons">
        <b-button class="is-primary" @click="toSearch" icon-left="magnify">Search</b-button>
        <b-button class="is-info" @click="clear" icon-left="autorenew">Clear</b-button>
      </div>
    </div>
    <br>
    <div class="columns is-mobile">
      <section class="column">
        <b-taglist attached>
          <b-tag type="is-dark" size="is-large">
            <b-icon
              icon="pound"
              size="is-small">
            </b-icon>
          </b-tag>
          <b-tag type="is-info" size="is-large">{{ ticket.id }}</b-tag>
        </b-taglist>
        <b-field
          label="From"
          label-position="on-border">
          <b-input required v-model="route.from" disabled></b-input>
        </b-field>
        <b-field
          label="To"
          label-position="on-border">
          <b-input required v-model="route.to" disabled></b-input>
        </b-field>
        <b-field
          label="Date"
          label-position="on-border">
          <b-input required v-model="route.date" disabled></b-input>
        </b-field>
        <b-field
          label="Seat"
          label-position="on-border">
          <b-input required v-model="ticket.seat" disabled></b-input>
        </b-field>
        <b-field
          label="Passenger name"
          label-position="on-border">
          <b-input required v-model="user.name" disabled></b-input>
        </b-field>
        <b-field
          label="Passenger last name"
          label-position="on-border">
          <b-input required v-model="user.last_name" disabled></b-input>
        </b-field>
        <b-field
          label="Passenger email"
          label-position="on-border">
          <b-input required v-model="user.email" disabled></b-input>
        </b-field>
        <b-field>
          <b-checkbox v-model="ticket.check_in" disabled>
            Check in
          </b-checkbox>
        </b-field>
        <div class="buttons">
          <b-button
            class="is-info"
            @click="checkInTicket"
            icon-left="check-bold"
            :disabled="ticket.id.length === 0 || ticket.check_in === true">Check In</b-button>
        </div>
      </section>
    </div>
  </section>
</template>

<script>
export default {
  middleware: ['check-auth', 'auth'],
  data() {
    return {
      ticketId: '',
      ticket: {
        id: '',
        seat: '',
        check_in: false
      },
      route: {
        from: '',
        to: '',
        date: '',
      },
      user: {
        name: '',
        last_name: '',
        email: ''
      }
    }
  },
  methods: {
    async toSearch() {
      if(this.ticketId.length === 0) {
        console.log('err')
        return
      }


      const ticket = await this.$axios.$get(`http://localhost:8080/api/v1/tickets/${this.ticketId}`, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `${this.$store.getters.getToken}`
        }
      })

      this.ticket = ticket
      this.route = ticket.route
      this.user = ticket.user

      console.log(ticket)

      // await this.$store.dispatch('flightsFounded', flights)
      // await this.$router.push({
      //   path: '/routes/result',
      // })
    },
    checkInTicket(){
      this.$axios.$put(
        `http://localhost:8080/api/v1/tickets/${this.ticket.id}`,
        {},
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `${this.$store.getters.getToken}`
          },
          params: {
            'check_in': true
          }
        }).then(() => {
        this.$buefy.toast.open({
          message: 'Check in completed!',
          type: 'is-success'
        })
      })

    },
    clear(){
      this.ticketId = ''
      this.ticket = {
        id: '',
        seat: '',
        check_in: false
      }
      this.route = {
        from: '',
        to: '',
        date: '',
      }
      this.user = {
        name: '',
        last_name: '',
        email: ''
      }
    }
  }
}
</script>
