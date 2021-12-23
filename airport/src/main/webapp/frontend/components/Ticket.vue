<template>
  <div class="column">
    <div class="card">
      <header class="card-header">
        <p class="card-header-title has-text-grey">
          {{ ticket.id }}
        </p>
      </header>
      <div class="card-content">
        <div class="content has-text-centered">
          <section class="columns">
            <div class="column">
              <div class="columns">
                <div class="column">
                  <p class="title">
                    Seat
                  </p>
                  <p class="subtitle">
                    {{ ticket.seat }}
                  </p>
                </div>
                <div class="column">
                  <p class="title">
                    Check in
                  </p>
                  <p class="subtitle">
                    {{ ticket.check_in }}
                  </p>
                </div>
<!--                <div class="column">-->
<!--                  <p class="title">-->
<!--                    {{ flightTime }}-->
<!--                  </p>-->
<!--                  <p class="subtitle">-->
<!--                    <code>Estimated flight time</code>-->
<!--                  </p>-->
<!--                </div>-->
              </div>
            </div>
            <div class="column">
              <p class="title">
                Route
              </p>
              <p class="subtitle">
                {{ ticketRoute.from }} - {{ ticketRoute.to  }}
              </p>
            </div>
          </section>
        </div>
      </div>
      <footer class="card-footer">
<!--        <a v-for="item in flight.airlines" @click="bookFlight" class="card-footer-item">Book {{ item.name }}</a>-->
      </footer>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    ticket: {
      type: Object,
      required: true
    },
  },
  data(){
    return{
      ticketRoute: {}
    }
  },
  created(){
    this.getTicketRoute()
  },
  // computed: {
  //   flightTime() {
  //     const start = new Date(this.flight.etd)
  //     const end = new Date(this.flight.eta)
  //     const hours = ((end.getTime() - start.getTime()) / 1000) / 3600
  //     return hours + 'h'
  //   }
  // },
  methods: {
    getTicketRoute(){
      this.ticketRoute = this.ticket.route
      console.log(this.ticketRoute)
    }
  }
}
</script>
