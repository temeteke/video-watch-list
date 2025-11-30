FROM node:18-alpine AS builder

WORKDIR /app

ARG NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
ENV NEXT_PUBLIC_API_URL=${NEXT_PUBLIC_API_URL}

COPY package.json ./
RUN npm install

COPY . .

RUN npm run build

# ========== Runtime Stage ==========
FROM node:18-alpine

WORKDIR /app

COPY package.json ./
RUN npm install --production

COPY --from=builder /app/.next ./.next

EXPOSE 3000

CMD ["npm", "start"]
